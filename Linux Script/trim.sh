#!/bin/bash
sized=$(sox $1.flac -n stat 2>&1 | sed -n 's#^Length (seconds):[^0-9]*\([0-9.]*\)$#\1#p')
div=$(echo $sized/15 | bc -l)
#echo $div

x=0
count=1
dif=$(echo $sized - $x | bc -l)
while (( $(echo "$dif > 0" | bc -l) ))
  do
    if (( $(echo "$dif > 14" | bc -l) )); then
    sox $1.flac $1$count.flac trim $x 14
    ((x=x+14))
    dif=$(echo $sized - $x | bc -l)
  else
    sox $1.flac $1$count.flac trim $x $dif
    break
  fi
  count=$(echo $count + 1 | bc -l)
done