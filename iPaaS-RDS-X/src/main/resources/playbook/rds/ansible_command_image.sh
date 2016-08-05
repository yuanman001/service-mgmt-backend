cd ${1} && /usr/bin/ansible-playbook -i ansible_ssh/${2}.cfg   rdsimage_command.yml  --user=${3} --extra-vars "user=${3} ansible_ssh_pass=${4} host=${5} command=${6} container_name=${7}"

