FROM ubuntu:16.04
WORKDIR /root
RUN set -ex \
    && apt-get update \
    && apt-get -y install software-properties-common \
    && add-apt-repository ppa:bubblenetwork/bubble
ADD config .
RUN set -ex \
    && apt-get update \
    && apt-get -y install bubble \
    && bubble --datadir ./data init bubble.json
ENTRYPOINT ["/usr/bin/bubble","--identity","bubble","--datadir","./data","--port","16789","--rpcaddr","0.0.0.0","--rpcport","6789","--rpcapi","db,eth,net,web3,admin,personal","--rpc","--nodiscover","--nodekey","./data/bubble/nodekey"]
