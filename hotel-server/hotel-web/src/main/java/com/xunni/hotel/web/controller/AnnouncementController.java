package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Announcement;
import com.xunni.hotel.web.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/announcements")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public Result getAnnouncements() {
        List<Announcement> announcements = announcementService.lambdaQuery()
                .orderByDesc(Announcement::getCreateTime)
                .list();
        return Result.success(announcements);
    }

    @PostMapping
    public Result createAnnouncement(@RequestBody Map<String, Object> params) {
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        String author = (String) params.get("author");

        if (title == null || title.isEmpty() || content == null || content.isEmpty()) {
            return Result.error("标题和内容不能为空");
        }

        Announcement announcement = new Announcement();
        announcement.setTitle(title);
        announcement.setContent(content);
        announcement.setAuthor(author);
        announcement.setStatus("active");
        announcement.setCreateTime(LocalDateTime.now());
        announcement.setUpdateTime(LocalDateTime.now());

        boolean success = announcementService.save(announcement);

        if (success) {
            return Result.success(announcement);
        } else {
            return Result.error("创建公告失败");
        }
    }

    @GetMapping("/{id}")
    public Result getAnnouncementById(@PathVariable Integer id) {
        Announcement announcement = announcementService.getById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }
        return Result.success(announcement);
    }

    @DeleteMapping("/{id}")
    public Result deleteAnnouncement(@PathVariable Integer id) {
        boolean success = announcementService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    @PutMapping("/{id}")
    public Result updateAnnouncement(@PathVariable Integer id, @RequestBody Map<String, Object> params) {
        Announcement announcement = announcementService.getById(id);
        if (announcement == null) {
            return Result.error("公告不存在");
        }

        String title = (String) params.get("title");
        String content = (String) params.get("content");

        if (title != null) {
            announcement.setTitle(title);
        }
        if (content != null) {
            announcement.setContent(content);
        }
        announcement.setUpdateTime(LocalDateTime.now());

        boolean success = announcementService.updateById(announcement);
        if (success) {
            return Result.success(announcement);
        } else {
            return Result.error("更新失败");
        }
    }
}
