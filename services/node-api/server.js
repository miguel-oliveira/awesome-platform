var express = require("express"),
  app = express(),
  port = process.env.PORT || 3000,
  bodyParser = require("body-parser");
const { Client } = require("pg");

const consul = require("consul")({
  host: "172.18.0.2"
});
const CONSUL_ID = require("uuid/v4")();

//Setup server
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

async function getDataFromDB(res) {
  const conn = {
    user: "postgres",
    host: known_data_instances[0].Node.Address,
    database: "test",
    password: "15931593",
    port: known_data_instances[0].Service.Port
  };
  console.log(conn);
  const client = new Client(conn);

  await client.connect();

  const result = await client.query("SELECT * FROM public.clients");
  res.json(result.rows.length);
  await client.end();
}

app.route("/foo").get(function(req, res) {
  getDataFromDB(res);
});

app.listen(port);

console.log("RESTful API server started on: " + port);
console.log("Address: " + process.env.ADDRESS);

//SETUP AS CONSUL CLIENT
let known_data_instances = [];

const watcher = consul.watch({
  method: consul.health.service,
  options: {
    service: "postgres",
    passing: true
  }
});

watcher.on("change", data => {
  console.log("listening to changes", data);
  known_data_instances = [];
  data.forEach(entry => {
    known_data_instances.push(entry);
  });
});

//REGISTER ON CONSUL
const options = {
  name: "node-api",
  // Works if we pass in the node id from the consul ui => '172.21.0.2'
  // and not using docker to run nodejs
  //address: "127.18.0.2",
  // port: 8500,
  id: CONSUL_ID,
  check: {
    ttl: "10s",
    deregister_critical_service_after: "1m"
  }
};

consul.agent.service.register(options, err => {
  if (err) {
    console.log(err);
    return;
  }
  // schedule heartbeat
  setInterval(() => {
    consul.agent.check.pass({ id: `service:${CONSUL_ID}` }, err => {
      if (err) {
        throw new Error(err);
      }
      console.log("told Consul that we are healthy");
    });
  }, 5 * 1000);
});

process.on("SIGINT", () => {
  console.log("SIGINT. De-Registering...");
  let details = { id: CONSUL_ID };

  consul.agent.service.deregister(details, err => {
    console.log("de-registered.", err);
    process.exit();
  });
});
