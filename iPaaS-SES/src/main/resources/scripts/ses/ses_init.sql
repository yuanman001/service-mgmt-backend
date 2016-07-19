--insert ses image info
insert ipaas_image_resource(id,service_code,image_repository,image_code,image_name,status) values(10,'ses','10.1.245.4:5000','ses','ipaas_ses:0.3','1');

--insert ses ssh user info
insert ipaas_sys_config(id,table_code,field_code,field_value) values(10,'ses','ses_ssh_user','schusr01');

insert ipaas_sys_config(id,table_code,field_code,field_value) values(11,'ses','ses_ssh_user_pwd','schusr01_7788');

commit;
