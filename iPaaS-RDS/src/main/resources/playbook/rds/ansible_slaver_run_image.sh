cd ${1} && /usr/bin/ansible-playbook -i ansible_ssh/${2}.cfg   rdsimage_slaver.yml  --user=${3} --extra-vars "user=${3} ansible_ssh_pass=${4} host=${5} image=${6} port=${7} mysql_data_path=${8} mysql_home=${9} mysql_volumn_path=${10} container_name=${11} container_storage=${13} mysql_type=${14} master_ip=${15} master_port=${16}  db_root_name=${17} db_root_password=${18} white_ip_list=${19}"