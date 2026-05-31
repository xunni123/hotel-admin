package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Cleaner;
import com.xunni.hotel.entity.CleaningTask;
import com.xunni.hotel.web.dto.CleanerWithTaskCountDTO;
import com.xunni.hotel.web.service.CleanerService;
import com.xunni.hotel.web.service.CleaningTaskService;
import com.xunni.hotel.web.service.RoomService;
import com.xunni.hotel.web.mapper.RoomMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cleaning")
public class CleaningController {

    @Autowired
    private CleanerService cleanerService;

    @Autowired
    private CleaningTaskService cleaningTaskService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomMapper roomMapper;

    @GetMapping("/cleaners")
    public Result getCleaners() {
        List<Cleaner> cleaners = cleanerService.list();
        return Result.success(cleaners);
    }

    @GetMapping("/tasks")
    public Result getTasks(@RequestParam(required = false) String status) {
        List<CleaningTask> tasks;
        if (status != null && !status.isEmpty()) {
            tasks = cleaningTaskService.lambdaQuery()
                    .eq(CleaningTask::getStatus, status)
                    .list();
        } else {
            tasks = cleaningTaskService.list();
        }
        return Result.success(tasks);
    }

    @PostMapping("/assign")
    public Result assignTask(@RequestBody Map<String, Object> params) {
        Integer cleanerId = (Integer) params.get("cleanerId");
        List<?> roomIds = (List<?>) params.get("roomIds");
        String assignedBy = (String) params.get("assignedBy");

        if (cleanerId == null || roomIds == null || roomIds.isEmpty()) {
            return Result.error("参数错误");
        }

        Cleaner cleaner = cleanerService.getById(cleanerId);
        if (cleaner == null) {
            return Result.error("保洁员不存在");
        }

        List<String> roomNumbers = new ArrayList<>();
        for (Object roomIdObj : roomIds) {
            Integer roomId;
            if (roomIdObj instanceof Integer) {
                roomId = (Integer) roomIdObj;
            } else if (roomIdObj instanceof String) {
                roomId = Integer.parseInt((String) roomIdObj);
            } else if (roomIdObj instanceof Number) {
                roomId = ((Number) roomIdObj).intValue();
            } else {
                return Result.error("roomId 类型错误");
            }

            String roomNumber = roomMapper.selectRoomNumberById(roomId);
            if (roomNumber != null) {
                roomNumbers.add(roomNumber);
            }

            CleaningTask task = new CleaningTask();
            task.setCleanerId(cleanerId);
            task.setRoomId(roomId);
            task.setStatus("pending");
            task.setAssignedBy(assignedBy);
            task.setAssignedTime(LocalDateTime.now());
            task.setCreateTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            cleaningTaskService.save(task);

            roomService.updateRoomStatusToPendingCleaning(roomId);
        }

        return Result.success("派单成功");
    }

    @PutMapping("/updateStatus/{taskId}")
    public Result updateTaskStatus(@PathVariable Integer taskId, @RequestBody Map<String, String> params) {
        String status = params.get("status");
        if (taskId == null || status == null) {
            return Result.error("参数错误");
        }

        CleaningTask task = cleaningTaskService.getById(taskId);
        if (task == null) {
            return Result.error("任务不存在");
        }

        task.setStatus(status);
        if ("in_progress".equals(status)) {
            task.setStartTime(LocalDateTime.now());
        } else if ("completed".equals(status)) {
            task.setCompletedTime(LocalDateTime.now());
            roomService.updateRoomStatusToDirty(task.getRoomId());
        }
        task.setUpdateTime(LocalDateTime.now());

        boolean success = cleaningTaskService.updateById(task);
        return success ? Result.success(task) : Result.error("更新失败");
    }

    @PutMapping("/revertRoomStatus/{roomId}")
    public Result revertRoomStatus(@PathVariable Integer roomId) {
        if (roomId == null) {
            return Result.error("参数错误");
        }

        CleaningTask task = cleaningTaskService.lambdaQuery()
                .eq(CleaningTask::getRoomId, roomId)
                .eq(CleaningTask::getStatus, "pending")
                .one();

        if (task != null) {
            cleaningTaskService.removeById(task.getTaskId());
        }

        roomService.updateRoomStatusToDirty(roomId);
        return Result.success("任务已删除，房间状态已恢复");
    }

    @GetMapping("/cleanerWithTaskCount")
    public Result getCleanerWithTaskCount() {
        List<Cleaner> cleaners = cleanerService.list();
        List<CleaningTask> activeTasks = cleaningTaskService.lambdaQuery()
                .in(CleaningTask::getStatus, "pending", "in_progress")
                .list();

        Map<Integer, Long> taskCountMap = new HashMap<>();
        for (CleaningTask task : activeTasks) {
            taskCountMap.merge(task.getCleanerId(), 1L, Long::sum);
        }

        List<CleanerWithTaskCountDTO> result = new ArrayList<>();
        for (Cleaner cleaner : cleaners) {
            CleanerWithTaskCountDTO dto = new CleanerWithTaskCountDTO();
            BeanUtils.copyProperties(cleaner, dto);
            dto.setTaskCount(taskCountMap.getOrDefault(cleaner.getCleanerId(), 0L));
            result.add(dto);
        }

        return Result.success(result);
    }
}
