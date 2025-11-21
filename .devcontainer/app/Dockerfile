# [Choice] Ubuntu version (use ubuntu-22.04 or ubuntu-18.04 on local arm64/Apple Silicon): ubuntu-22.04, ubuntu-20.04, ubuntu-18.04
# https://github.com/microsoft/vscode-dev-containers/blob/main/containers/ubuntu/.devcontainer/Dockerfile
ARG VARIANT=ubuntu-22.04
FROM mcr.microsoft.com/vscode/devcontainers/base:0-${VARIANT}

# [Optional] Uncomment this section to install additional OS packages.
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install --no-install-recommends \
    curl

# Java
# https://docs.aws.amazon.com/corretto/latest/corretto-25-ug/generic-linux-install.html
RUN wget -O - https://apt.corretto.aws/corretto.key | sudo gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg && \
    echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | sudo tee /etc/apt/sources.list.d/corretto.list
RUN sudo apt-get update; sudo apt-get install -y java-25-amazon-corretto-jdk

# Nodejs 24
# https://github.com/nodesource/distributions#installation-instructions
RUN curl -fsSL https://deb.nodesource.com/setup_24.x -o nodesource_setup.sh
RUN sudo -E bash nodesource_setup.sh
RUN sudo apt-get install -y nodejs
RUN rm nodesource_setup.sh

# Bun
# https://bun.sh/
RUN curl -fsSL https://bun.sh/install | bash -s "bun-v1.3.2"
