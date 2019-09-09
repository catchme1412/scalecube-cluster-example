* To build the code ./build.sh
* To start the Proxy.java use ./proxy.sh
* To start the Agent.java use ./agent.sh

Following are the JVM arguments used by each services
-DPROXY_SERVICE_IP=localhost \
-DPROXY_SERVICE_PORT=4801 \
-DPROXY_TRANSPORT_PORT=4802 \

Please modify the VM arguments based on the nodes where you are running the proxy service.

Proxy will timeout n 30 sec and agent will timeout in 15 sec.

