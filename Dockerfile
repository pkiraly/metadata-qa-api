FROM ubuntu:24.04

RUN apt-get update && \
    apt-get install -y apt-utils openjdk-11-jre-headless && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt/mqaf

RUN  mkdir /opt/mqaf/input \
  && mkdir /opt/mqaf/output

COPY target/metadata-qa-api-0.9.9-SNAPSHOT-shaded.jar /opt/mqaf
COPY mqa /opt/mqaf

CMD ["./mqa"]