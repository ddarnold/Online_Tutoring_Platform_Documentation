# Use the official Ubuntu base image
FROM ubuntu:24.04

# Update package lists and install necessary tools
RUN apt-get update && apt-get upgrade -y
RUN apt-get install -y wget curl gnupg2 software-properties-common

# Create a new user named 'developer' and add to sudoers
RUN useradd -ms /bin/bash developer && \
    echo 'developer:password' | chpasswd && \
    usermod -aG sudo developer && \
    echo 'developer ALL=(ALL) NOPASSWD:ALL' >> /etc/sudoers

# Download JDK 21 and its checksum
RUN wget -q "https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb" -O jdk-21_linux-x64_bin.deb && \
    wget -q "https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb.sha256" -O jdk-21_linux-x64_bin.deb.sha256

# Verify the SHA-256 checksum
# RUN sha256sum -c jdk-21_linux-x64_bin.deb.sha256
# Extract the checksum from the downloaded checksum file and use it for verification
RUN actual_checksum=$(sha256sum jdk-21_linux-x64_bin.deb | awk '{print $1}') && \
    expected_checksum=$(cat jdk-21_linux-x64_bin.deb.sha256) && \
    if [ "$actual_checksum" = "$expected_checksum" ]; then echo "Checksum verified."; else echo "Checksum verification failed!" >&2; exit 1; fi

# Install JDK
RUN apt-get install ./jdk-21_linux-x64_bin.deb

# Install Node.js 22 and npm
RUN curl -fsSL "https://deb.nodesource.com/setup_22.x" | bash - && \
    apt-get install -y nodejs

# Install PostgreSQL
RUN apt-get install -y postgresql

# Install vsCode
# RUN wget -q "https://code.visualstudio.com/sha/download?build=stable&os=linux-deb-x64" -O code_1.95.2-1730981514_amd64.deb
# RUN apt-get install -y ./code_1.95.2-1730981514_amd64.deb

# Clean up
RUN rm jdk-21_linux-x64_bin.deb jdk-21_linux-x64_bin.deb.sha256
# RUN rm code_1.95.2-1730981514_amd64.deb

# Expose relevant ports (e.g., for PostgreSQL)
# EXPOSE 5432

# Switch to 'developer'
USER developer

# Set the default working directory and command
WORKDIR /home/developer

# Copy the project into the image
COPY . /home/developer/app

# Default command to keep it running
CMD ["bash"]
# TODO: eventually run the app
