#set terminal postscript eps enhanced color size 3,2.5 "NimbusSanL-Regu,17" fontfile "/usr/share/texmf/fonts/type1/urw/helvetic/uhvr8a.pfb"
print "\n\nNow using variable values: " 
print outname
print title

set terminal pdf
set out outname
set title title
set xlabel distance
set ylabel error
set key off
set logscale y 10
plot filename using 1:2 w points lt rgb "black", filename using 1:2:3 with errorbars lt rgb "black"
