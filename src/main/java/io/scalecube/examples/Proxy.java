package io.scalecube.examples;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;

public class Proxy {

  /** Main method. */
  public static void main(String[] args) throws Exception {

    String proxyIp = System.getProperty("PROXY_SERVICE_IP", "localhost");
    int proxyPort = Integer.parseInt(System.getProperty("PROXY_SERVICE_PORT", "4801") );
    int proxyTransportPort = Integer.parseInt(System.getProperty("PROXY_TRANSPORT_PORT", "4802"));
    Cluster proxy =
        new ClusterImpl()
            .config(config -> config.membership(opts -> opts.seedMembers(Address
              .create(proxyIp, proxyPort)))
              .transport(cfg -> cfg.port(proxyTransportPort))
            )
            .handler(
                cluster -> {
                  return new ClusterMessageHandler() {
                    @Override
                    public void onMessage(Message msg) {
                      System.out.println("************************************************");
                      System.out.println("proxy received: " + msg.data());
                      System.out.println("************************************************");
                      cluster
                          .send(msg.sender(), Message.fromData("Greetings from proxy"))
                          .subscribe(null, Throwable::printStackTrace);
                    }
                  };
                })
            .startAwait();


    // Avoid exit main thread immediately ]:->
    Thread.sleep(30000);
  }
}
