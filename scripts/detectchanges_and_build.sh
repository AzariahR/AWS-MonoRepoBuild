#!/bin/bash -e

echo "Gonna Try build for the Commit ID - " ${CODEBUILD_RESOLVED_SOURCE_VERSION}

git --version

parentId=`aws codecommit batch-get-commits --repository-name AzarDemoRepo  --commit-ids ${CODEBUILD_RESOLVED_SOURCE_VERSION} | jq -r '.commits[0].parents[0]'`

echo "ParentId (Previous Commit ID):" $parentId

diffJson=`aws codecommit get-differences --repository-name AzarDemoRepo --before-commit-specifier $parentId --after-commit-specifier ${CODEBUILD_RESOLVED_SOURCE_VERSION}`

echo "DIFFS -" $diffJson

diffSize=`echo $diffJson | jq '.differences | length'`

echo $diffSize " file(s) changed in the Commit ID - " ${CODEBUILD_RESOLVED_SOURCE_VERSION}

for microservice in $(echo $diffJson | jq -r '.differences[] | .afterBlob.path' | awk "/apps/" | cut -f 2 -d "/" | uniq)
do
    echo "Changes deteted in - " $microservice ". Triggering build for " $microservice
    
    if [ "$microservice" == 'MonoFirstApp' ]; then
        aws codebuild start-build --project-name FirstAppBuild --privileged-mode-override
    fi
    
    if [ "$microservice" == 'MonoSecondApp' ]; then
        aws codebuild start-build --project-name SecondAppBuild --privileged-mode-override
    fi   
done
