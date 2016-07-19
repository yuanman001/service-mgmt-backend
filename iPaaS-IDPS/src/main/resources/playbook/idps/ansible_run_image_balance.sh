cd $8 && /usr/bin/ansible-playbook -i ansible_ssh/$1.cfg   idpsimagebalance.yml  --user=$2 --extra-vars "ansible_ssh_pass=$3 host=$4 user=$2 image=$5 port=$6 server_list=$7 idps_container_name=${9}"

