create database msc_development ;
create table cmodule
(
    id_module int auto_increment
        primary key,
    active bit null,
    date_create datetime(6) null,
    date_delete datetime(6) null,
    date_update datetime(6) null,
    path varchar(255) null,
    name varchar(255) null
)
    engine=InnoDB;

create table locality
(
    id_locality int auto_increment
        primary key,
    cp varchar(255) null,
    name varchar(255) null,
    id_municipality int null
)
    engine=InnoDB;

create table municipality
(
    id_municipality int auto_increment
        primary key,
    name varchar(255) null,
    state int null
)
    engine=InnoDB;

alter table locality
    add constraint FK8wc197tffd0ewaufqliekifdk
        foreign key (id_municipality) references municipality (id_municipality);

create table state
(
    id_state int auto_increment
        primary key,
    name varchar(255) null
)
    engine=InnoDB;

alter table municipality
    add constraint FKguw77kwn6kno7xnaajjps4ix6
        foreign key (state) references state (id_state);

create table tlog_pass
(
    id_log_pass int not null,
    active bit null,
    date_create datetime(6) null,
    date_delete datetime(6) null,
    date_update datetime(6) null,
    expired bit null,
    password varchar(255) null,
    id_user int null
)
    engine=InnoDB;

alter table tlog_pass
    add primary key (id_log_pass);

create table tmodule_profile
(
    id_module_profile int auto_increment
        primary key,
    active bit null,
    date_create datetime(6) null,
    date_delete datetime(6) null,
    date_update datetime(6) null,
    id_module int null,
    id_profile int null
)
    engine=InnoDB;

alter table tmodule_profile
    add constraint FKlfbsxpannjvlsv0go2owphl06
        foreign key (id_module) references cmodule (id_module);

create table tprofile
(
    id_profile int auto_increment
        primary key,
    active bit null,
    date_create datetime(6) null,
    date_delete datetime(6) null,
    date_update datetime(6) null,
    key_profile varchar(255) null,
    name varchar(255) null
)
    engine=InnoDB;

alter table tmodule_profile
    add constraint FK5lvwyblqw88st8barxanevkl7
        foreign key (id_profile) references tprofile (id_profile);

create table tuser
(
    id_user int auto_increment
        primary key,
    active bit null,
    date_create datetime(6) null,
    date_delete datetime(6) null,
    date_update datetime(6) null,
    age int null,
    birth_date datetime(6) null,
    email varchar(255) null,
    last_name varchar(255) null,
    middle_name varchar(255) null,
    name varchar(255) not null,
    phone_number varchar(255) null,
    user_name varchar(255) null,
    profile int null
)
    engine=InnoDB;

alter table tlog_pass
    add constraint FKh6un03x6y30i2oa2jifta9jkl
        foreign key (id_user) references tuser (id_user);

alter table tuser
    add constraint FKwwmlq2hi9h580vv2q8p3b2t
        foreign key (profile) references tprofile (id_profile);


DELIMITER //
CREATE PROCEDURE search_username(IN username varchar(100), out count_of_users int)
begin

    declare exit handler for sqlexception

        begin

            rollback;

            resignal;

        end;

    declare exit handler for sqlwarning

        begin

            rollback;

            resignal;

        end;


    select count(user_name) into count_of_users from tuser where user_name = username;


end//
DELIMITER ;




