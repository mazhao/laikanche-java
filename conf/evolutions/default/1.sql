# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table car_brand (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  constraint pk_car_brand primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table car_brand;

SET FOREIGN_KEY_CHECKS=1;

