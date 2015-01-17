# --- !Ups
alter table car_video add column screen_file_content_type  varchar(255);
alter table car_video add column screen_file_full_path     varchar(255);

# --- !Downs

alter table car_video drop column screen_file_content_type;
alter table car_video drop column screen_file_full_path;

