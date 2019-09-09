package io.scalecube.examples;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.ClusterImpl;
import io.scalecube.cluster.ClusterMessageHandler;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import reactor.core.publisher.Flux;

public class Agent {

  /** Main method. */
  public static void main(String[] args) throws Exception {

    String proxyIp = System.getProperty("PROXY_SERVICE_IP", "localhost");
    int proxyPort = Integer.parseInt(System.getProperty("PROXY_SERVICE_PORT", "4801"));
    int proxyTransportPort = Integer.parseInt(System.getProperty("PROXY_TRANSPORT_PORT", "4802"));
    // Join cluster node Agent to cluster with Proxy
    Cluster agent =
        new ClusterImpl()
            .config(
                config ->
                    config.membership(opts -> opts.seedMembers(Address.create(proxyIp, proxyTransportPort)))
//                      .transport(cfg -> cfg.port(proxyTransportPort))
            )
            .handler(
                cluster -> {
                  return new ClusterMessageHandler() {
                    @Override
                    public void onMessage(Message msg) {
                      System.out.println("************************************************");
                      System.out.println("Agent received: " + msg.data());
                      System.out.println("************************************************");
                    }
                  };
                })
            .startAwait();

    Message greetingMsg = Message.fromData("Greetings from agent");

    Flux.fromIterable(agent.otherMembers())
        .flatMap(member -> agent.send(member, greetingMsg))
        .subscribe(null, Throwable::printStackTrace);

    // Avoid exit main thread immediately ]:->
    Thread.sleep(15000);
  }
}
