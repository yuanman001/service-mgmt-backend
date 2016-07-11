cd ${1} && /usr/bin/ansible-playbook -i ansible_ssh/${2}.cfg   ses.yml  --user=${3} --extra-vars "ansible_ssh_pass=${4} host=${5} user=${6} image=${7} USER_PID=${8} SES_SRV_ID=${9} SRV_PORT=${10} SRV_HTTP_PORT=${10} SES_CLUSTER=${11} SRV_HOST=${12}"

