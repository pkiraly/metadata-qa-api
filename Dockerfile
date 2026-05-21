FROM ubuntu:24.04

LABEL maintainer="Péter Király <pkiraly@gwdg.de>"

LABEL description="MQAF - Metadata Quality Assessment Framework API."
# the Github repo labels
LABEL org.opencontainers.image.description="MQAF - Metadata Quality Assessment Framework API."
LABEL org.opencontainers.image.source=https://github.com/pkiraly/metadata-qa-api
LABEL org.opencontainers.image.licenses="GNU General Public License v3.0"

RUN apt-get update && \
    apt-get install -y apt-utils openjdk-11-jre-headless && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt/mqaf

RUN  mkdir /opt/mqaf/input \
  && mkdir /opt/mqaf/output

COPY target/metadata-qa-api-0.9.9-SNAPSHOT-shaded.jar /opt/mqaf
COPY mqa /opt/mqaf

CMD ["./mqa"]