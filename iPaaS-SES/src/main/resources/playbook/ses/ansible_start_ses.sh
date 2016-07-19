cd ${1} && /usr/bin/ansible-playbook -i ansible_ssh/${2}.cfg   ses_start.yml  --user=${3} --extra-vars "ansible_ssh_pass=${4} host=${5} user=${6} USER_PID=${7} SES_SRV_ID=${8} SRV_HTTP_PORT=${9}"

