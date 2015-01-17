# --- !Ups
alter table car_video DROP column screen_file_full_path;
alter table car_video add column screen_file_name     varchar(255);

# --- !Downs


