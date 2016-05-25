/usr/bin/ansible-playbook -i $HOME/idps/ansible_ssh/$1.cfg   $HOME/idps/idpsimage.yml  --user=$2 --extra-vars "ansible_ssh_pass=$3 host=$4 user=$2 image=$5 port=$6 auth_url=$7 auth_user_pid=$8 auth_srv_id=$9 auth_srv_pwd=$10"

