CREATE TABLE `tag_video` (
  `tag_id` BIGINT(20) NOT NULL,
  `video_id` BIGINT(20) NOT NULL,
  PRIMARY KEY (`tag_id`, `video_id`));
