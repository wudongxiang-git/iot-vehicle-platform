package com.iot.vehicle.simulator.controller;

import com.iot.vehicle.simulator.DeviceSimulator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 模拟器控制接口
 *
 * @author dongxiang.wu
 */
@RestController
@RequestMapping("/simulator")
@RequiredArgsConstructor
public class SimulatorController {

    private final DeviceSimulator deviceSimulator;

    @PostMapping("/start")
    public Map<String, Object> start() {
        deviceSimulator.start();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "started");
        result.put("message", "模拟器已启动");
        return result;
    }

    @PostMapping("/stop")
    public Map<String, Object> stop() {
        deviceSimulator.stop();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "stopped");
        result.put("message", "模拟器已停止");
        return result;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "ok");
        return result;
    }
}

