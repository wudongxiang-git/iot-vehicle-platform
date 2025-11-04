#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
MQTT测试客户端
用于模拟设备发送MQTT消息

作者: dongxiang.wu
使用: python3 mqtt-test-client.py
"""

import paho.mqtt.client as mqtt
import json
import time
import random

# MQTT配置
BROKER = "localhost"
PORT = 1883
DEVICE_ID = "DEV_TEST_001"  # 使用数据库中的测试设备ID
SECRET_KEY = "test_secret_001"

# 连接回调
def on_connect(client, userdata, flags, rc):
    if rc == 0:
        print(f"✅ 连接成功！设备ID: {DEVICE_ID}")
        # 发送上线消息
        send_online_message(client)
    else:
        print(f"❌ 连接失败，返回码: {rc}")

# 消息回调
def on_message(client, userdata, msg):
    print(f"📨 收到消息: {msg.topic} - {msg.payload.decode()}")

# 发送上线消息
def send_online_message(client):
    topic = f"device/{DEVICE_ID}/status"
    payload = {
        "status": "online",
        "ip": "192.168.1.100",
        "timestamp": int(time.time() * 1000)
    }
    client.publish(topic, json.dumps(payload))
    print(f"📤 发送上线消息: {topic}")

# 发送设备数据
def send_device_data(client):
    topic = f"device/{DEVICE_ID}/data"
    payload = {
        "speed": random.randint(0, 120),
        "rpm": random.randint(800, 5000),
        "fuel": random.randint(0, 100),
        "temperature": random.randint(20, 90),
        "timestamp": int(time.time() * 1000)
    }
    client.publish(topic, json.dumps(payload))
    print(f"📤 发送设备数据: {json.dumps(payload)}")

# 发送位置数据
def send_location_data(client):
    topic = f"device/{DEVICE_ID}/location"
    payload = {
        "lat": 31.23 + random.uniform(-0.01, 0.01),
        "lng": 121.47 + random.uniform(-0.01, 0.01),
        "speed": random.randint(0, 120),
        "direction": random.randint(0, 360),
        "timestamp": int(time.time() * 1000)
    }
    client.publish(topic, json.dumps(payload))
    print(f"📤 发送位置数据: lat={payload['lat']:.4f}, lng={payload['lng']:.4f}")

# 发送心跳
def send_heartbeat(client):
    topic = f"device/{DEVICE_ID}/heartbeat"
    payload = {
        "timestamp": int(time.time() * 1000)
    }
    client.publish(topic, json.dumps(payload))
    print(f"💓 发送心跳")

def main():
    print("=" * 50)
    print("IoT Vehicle Platform - MQTT测试客户端")
    print("=" * 50)
    print(f"设备ID: {DEVICE_ID}")
    print(f"Broker: {BROKER}:{PORT}")
    print("=" * 50)
    
    # 创建MQTT客户端
    client = mqtt.Client(client_id=DEVICE_ID)
    client.on_connect = on_connect
    client.on_message = on_message
    
    # 设置认证（如果需要）
    # client.username_pw_set(DEVICE_ID, SECRET_KEY)
    
    # 连接到Broker
    try:
        client.connect(BROKER, PORT, 60)
        client.loop_start()
        
        # 持续发送测试数据
        counter = 0
        while True:
            counter += 1
            print(f"\n--- 第 {counter} 次发送 ---")
            
            # 每次发送不同类型的消息
            send_device_data(client)
            time.sleep(2)
            
            send_location_data(client)
            time.sleep(2)
            
            send_heartbeat(client)
            time.sleep(5)
            
    except KeyboardInterrupt:
        print("\n\n⏹️  停止发送...")
        # 发送离线消息
        topic = f"device/{DEVICE_ID}/status"
        payload = {
            "status": "offline",
            "timestamp": int(time.time() * 1000)
        }
        client.publish(topic, json.dumps(payload))
        print("📤 发送离线消息")
        time.sleep(1)
        
        client.loop_stop()
        client.disconnect()
        print("👋 已断开连接")

if __name__ == "__main__":
    main()

