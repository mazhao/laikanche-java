# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table car_brand (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_car_brand primary key (id))
;

create table car_series (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  car_brand_id              bigint,
  constraint pk_car_series primary key (id))
;

create table car_video (
  id                        bigint auto_increment not null,
  car_series_id             bigint,
  title                     varchar(255),
  from_web                  varchar(255),
  url                       varchar(255),
  reporter                  varchar(255),
  create_date               datetime,
  count_good                bigint,
  count_bad                 bigint,
  constraint pk_car_video primary key (id))
;

alter table car_series add constraint fk_car_series_carBrand_1 foreign key (car_brand_id) references car_brand (id) on delete restrict on update restrict;
create index ix_car_series_carBrand_1 on car_series (car_brand_id);
alter table car_video add constraint fk_car_video_carSeries_2 foreign key (car_series_id) references car_series (id) on delete restrict on update restrict;
create index ix_car_video_carSeries_2 on car_video (car_series_id);



# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table car_brand;

drop table car_series;

drop table car_video;

SET FOREIGN_KEY_CHECKS=1;

