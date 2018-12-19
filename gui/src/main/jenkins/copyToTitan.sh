#!/bin/sh
echo "Create shared folder"
mkdir /tmp/titan
echo "mount shared folder"
mount.cifs //titan/KiddyWatch /tmp/titan -o credentials=/var/lib/jenkins/jobs/KiddyWatch\ GUI/workspace/src/main/jenkins/credentials
echo "delete old files"
rm -R /tmp/titan/lib
mkdir /tmp/titan/lib
echo "copy files to shared folder"
cp /var/lib/jenkins/jobs/KiddyWatch\ GUI/workspace/target/jfx/app/KiddyWatchGui*.jar /tmp/titan/KiddyWatchGui.jar
cp -r /var/lib/jenkins/jobs/KiddyWatch\ GUI/workspace/target/jfx/app/lib/*.jar /tmp/titan/lib/
echo "unmount shared folder"
umount /tmp/titan
echo "delete shared folder"
rm -R /tmp/titan

