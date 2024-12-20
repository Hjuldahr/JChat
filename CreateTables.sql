DROP DATABASE IF EXISTS JChat;
CREATE DATABASE IF NOT EXISTS JChat;
USE JChat;

CREATE TABLE JChatUsers (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE JChatGroups (
    group_id INT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE JChatChannels (
    channel_id INT AUTO_INCREMENT PRIMARY KEY,
    channel_name VARCHAR(255) NOT NULL,
    group_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES JChatGroups(group_id) ON DELETE CASCADE,
    UNIQUE (group_id, channel_name)
);

CREATE TABLE JChatGroupUsers (
    group_user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    group_id INT NOT NULL,
    role ENUM('member', 'admin') DEFAULT 'member',
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES JChatUsers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES JChatGroups(group_id) ON DELETE CASCADE,
    UNIQUE (user_id, group_id)
);

CREATE TABLE JChatGroupChannels (
    group_channel_id INT AUTO_INCREMENT PRIMARY KEY,
    group_id INT NOT NULL,
    channel_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES JChatGroups(group_id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES JChatChannels(channel_id) ON DELETE CASCADE,
    UNIQUE (group_id, channel_id)
);

CREATE TABLE JChatChannelUsers (
    channel_user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    channel_id INT NOT NULL,
    group_id INT NOT NULL,
    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES JChatUsers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES JChatChannels(channel_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES JChatGroups(group_id) ON DELETE CASCADE,
    UNIQUE (user_id, channel_id, group_id)
);

CREATE TABLE JChatMessages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    channel_id INT NOT NULL,
    group_id INT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES JChatUsers(user_id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES JChatChannels(channel_id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES JChatGroups(group_id) ON DELETE CASCADE
);