#!/bin/bash -e

echo "Gonna STOP old Second App instances"

currentRunningInstances=`aws ecs list-tasks --cluster MumbaiCluster --family BLUESecondAppTaskDefinition`

noOfRunningSecondAppInstances=`echo $currentRunningInstances | jq -r '.taskArns | length'`

echo $noOfRunningSecondAppInstances " SecondApp instance(s) are running now."

for task in $(echo $currentRunningInstances | jq -r '.taskArns[]' | cut -f 2 -d "/")
do
	echo STOPPING - $task
	aws ecs stop-task --cluster MumbaiCluster --task $task
done
