const consul = require("consul")({
  host: "172.18.0.2"
});
const { Client } = require("pg");
const CONSUL_ID = require("uuid/v4")();

async function checkDBIsOnline(success, fail) {
  try {
    const conn = {
      user: "postgres",
      host: "172.18.0.3",
      database: "test",
      password: "15931593",
      port: 5432,
      statement_timeout: 1000
    };
    console.log(conn);
    const client = new Client(conn);

    await client.connect();

    success();
    await client.end();
  } catch (error) {
    console.log("error ocurred");
    fail();
  }
}

const options = {
  name: "postgres",
  address: "172.18.0.3",
  port: 5432,
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
    checkDBIsOnline(
      () => {
        consul.agent.check.pass({ id: `service:${CONSUL_ID}` }, err => {
          if (err) {
            throw new Error(err);
          }
          console.log("told Consul that POSTGRES is HEALTHY 👍");
        });
      },
      () => {
        consul.agent.check.fail({ id: `service:${CONSUL_ID}` }, err => {
          if (err) {
            throw new Error(err);
          }
          console.log("told Consul that POSTGRES is DOWN 👎");
        });
      }
    );
  }, 10 * 1000);
});
