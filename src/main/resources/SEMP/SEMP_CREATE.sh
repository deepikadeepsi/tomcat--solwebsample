#!/bin/bash

cd `dirname $0`
cmd=`basename $0`

if [[ $# -ne 3 ]]; then
	echo ""
	echo "	USAGE: $cmd <vmr-address> <admin-user> <admin-password>"
	echo ""
	exit 1
fi

host=$1
id="$2:$3"

echo ""
echo "Creating sample JCF"
curl -X POST -u $id http://$host:8080/SEMP -d @create-jcf.xml

echo ""
echo "Creating JNDI topic and mapping it to physical topic"
curl -X POST -u $id http://$host:8080/SEMP -d @create-jndi-topic.xml
curl -X POST -u $id http://$host:8080/SEMP -d @jndi-topic-phys.xml

echo ""
echo "Creating physical sample_queue"
curl -X POST -u $id -H 'content-type: application/json' http://$host:8080/SEMP/v2/config/msgVpns/default/queues -d @sample_queue.json

echo ""
echo "Creating JNDI queue and mapping it to physical sample_queue"
curl -X POST -u $id http://$host:8080/SEMP -d @create-jndi-queue.xml
curl -X POST -u $id http://$host:8080/SEMP -d @jndi-queue-phys.xml
