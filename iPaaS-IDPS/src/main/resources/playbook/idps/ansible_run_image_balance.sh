echo $7
/usr/bin/ansible-playbook -i $HOME/idps/ansible_ssh/$1.cfg   $HOME/idps/idpsimagebalance.yml  --user=$2 --extra-vars "ansible_ssh_pass=$3 host=$4 user=$2 image=$5 port=$6 server_list=$7"

