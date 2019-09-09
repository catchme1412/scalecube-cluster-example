java -cp target/*:target/dependency/* \
-DPROXY_SERVICE_IP=localhost \
-DPROXY_SERVICE_PORT=4801 \
-DPROXY_TRANSPORT_PORT=4802 \
io.scalecube.examples.Agent

