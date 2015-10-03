
#!/bin/sh
#$ -S /bin/sh
#$ -N nwen303_test
#$ -wd /vol/grid-solar/sgeusers/leatseng
#$ -pe nwen303_1.pe 4
#
echo ==UNAME==
uname -n
/usr/pkg/bin/mpirun -np $NSLOTS \
 /usr/pkg/java/sun-7/bin/java   \
   -classpath /u/students/leatseng/NWEN303_2015T2/OpenMPI-1100-examples \
 Ring






