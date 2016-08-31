# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table campoentity (
  id                            bigint not null,
  name                          varchar(255),
  region_id                     bigint,
  constraint pk_campoentity primary key (id)
);
create sequence Campo;

create table informeentity (
  id                            bigint not null,
  tipo                          varchar(255),
  dato                          float,
  fecha                         timestamp,
  emergencia                    boolean,
  sensor_id                     bigint,
  constraint pk_informeentity primary key (id)
);
create sequence Informe;

create table oilcolentity (
  id                            bigint not null,
  constraint pk_oilcolentity primary key (id)
);
create sequence OilCol;

create table pozoentity (
  id                            bigint not null,
  estado                        integer,
  campo_id                      bigint,
  constraint ck_pozoentity_estado check (estado in (0,1,2,3)),
  constraint pk_pozoentity primary key (id)
);
create sequence Pozo;

create table regionentity (
  id                            bigint not null,
  name                          varchar(255),
  oil_col_id                    bigint,
  constraint pk_regionentity primary key (id)
);
create sequence Region;

create table reporteentity (
  id                            bigint not null,
  date                          timestamp,
  constraint pk_reporteentity primary key (id)
);
create sequence Reporte;

create table sensorentity (
  id                            bigint not null,
  tipo                          varchar(255),
  pozo_id                       bigint,
  constraint pk_sensorentity primary key (id)
);
create sequence Sensor;

create table usuarioentity (
  id                            bigint not null,
  name                          varchar(255),
  tipo                          integer,
  oil_col_id                    bigint,
  campo_id                      bigint,
  constraint ck_usuarioentity_tipo check (tipo in (0,1)),
  constraint uq_usuarioentity_campo_id unique (campo_id),
  constraint pk_usuarioentity primary key (id)
);
create sequence Usuario;

alter table campoentity add constraint fk_campoentity_region_id foreign key (region_id) references regionentity (id) on delete restrict on update restrict;
create index ix_campoentity_region_id on campoentity (region_id);

alter table informeentity add constraint fk_informeentity_sensor_id foreign key (sensor_id) references sensorentity (id) on delete restrict on update restrict;
create index ix_informeentity_sensor_id on informeentity (sensor_id);

alter table pozoentity add constraint fk_pozoentity_campo_id foreign key (campo_id) references campoentity (id) on delete restrict on update restrict;
create index ix_pozoentity_campo_id on pozoentity (campo_id);

alter table regionentity add constraint fk_regionentity_oil_col_id foreign key (oil_col_id) references oilcolentity (id) on delete restrict on update restrict;
create index ix_regionentity_oil_col_id on regionentity (oil_col_id);

alter table sensorentity add constraint fk_sensorentity_pozo_id foreign key (pozo_id) references pozoentity (id) on delete restrict on update restrict;
create index ix_sensorentity_pozo_id on sensorentity (pozo_id);

alter table usuarioentity add constraint fk_usuarioentity_oil_col_id foreign key (oil_col_id) references oilcolentity (id) on delete restrict on update restrict;
create index ix_usuarioentity_oil_col_id on usuarioentity (oil_col_id);

alter table usuarioentity add constraint fk_usuarioentity_campo_id foreign key (campo_id) references campoentity (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists campoentity drop constraint if exists fk_campoentity_region_id;
drop index if exists ix_campoentity_region_id;

alter table if exists informeentity drop constraint if exists fk_informeentity_sensor_id;
drop index if exists ix_informeentity_sensor_id;

alter table if exists pozoentity drop constraint if exists fk_pozoentity_campo_id;
drop index if exists ix_pozoentity_campo_id;

alter table if exists regionentity drop constraint if exists fk_regionentity_oil_col_id;
drop index if exists ix_regionentity_oil_col_id;

alter table if exists sensorentity drop constraint if exists fk_sensorentity_pozo_id;
drop index if exists ix_sensorentity_pozo_id;

alter table if exists usuarioentity drop constraint if exists fk_usuarioentity_oil_col_id;
drop index if exists ix_usuarioentity_oil_col_id;

alter table if exists usuarioentity drop constraint if exists fk_usuarioentity_campo_id;

drop table if exists campoentity cascade;
drop sequence if exists Campo;

drop table if exists informeentity cascade;
drop sequence if exists Informe;

drop table if exists oilcolentity cascade;
drop sequence if exists OilCol;

drop table if exists pozoentity cascade;
drop sequence if exists Pozo;

drop table if exists regionentity cascade;
drop sequence if exists Region;

drop table if exists reporteentity cascade;
drop sequence if exists Reporte;

drop table if exists sensorentity cascade;
drop sequence if exists Sensor;

drop table if exists usuarioentity cascade;
drop sequence if exists Usuario;

