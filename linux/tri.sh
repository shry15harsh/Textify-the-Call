#!/bin/bash
sized=$(sox file.flac -n stat 2>&1 | sed -n 's#^Length (seconds):[^0-9]*\([0-9.]*\)$#\1#p')
div=$(echo $sized/15 | bc -l)
#echo $div

x=0
count=1
dif=$(echo $sized - $x | bc -l)
while (( $(echo "$dif > 0" | bc -l) ))
  do
    if (( $(echo "$dif > 2" | bc -l) )); then
    sox file.flac t$count.flac trim $x 2
    ((x=x+2))
    dif=$(echo $sized - $x | bc -l)
  else
    sox file.flac t$count.flac trim $x $dif
    break
  fi
  count=$(echo $count + 1 | bc -l)
done