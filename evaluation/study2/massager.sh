#rm data.csv
#rm data_i.csv

mkdir polished
mkdir merged
cd dumps

for f in *; do
  mkdir ../polished/$f
  cd $f
  for f2 in *; do
    sed 's/%//' $f2 > ../../polished/$f/$f2
  done
  new_file=../../merged/$f.csv
  cat ../../polished/$f/* > $new_file

  sed '/cpu,heap/d' $new_file > new_file_temp
  echo 'cpu,heap' >> temp_file
  cat new_file_temp >> temp_file
  cp temp_file $new_file
  rm temp_file
  rm new_file_temp

  cd ..
done

rm -r ../polished

exit

# cd ../polished
#
# for f in *; do
#   if [[ "$f" =~ "Instr" ]]; then
#     cat $f >> ../data_i_temp.csv
#   else
#     cat $f >> ../data_temp.csv
#   fi
# done
#
# rm -r ../polished
#
# cd ..
#
# sed '/cpu,heap/d' data_temp.csv > data2_temp.csv
# echo 'cpu,heap' >> temp_file
# cat data2_temp.csv >> temp_file
# cp temp_file data2_temp.csv
# rm temp_file
#
# sed '/cpu,heap/d' data_i_temp.csv > data_i2_temp.csv
# echo 'cpu,heap' >> temp_file
# cat data_i2_temp.csv >> temp_file
# cp temp_file data_i2_temp.csv
# rm temp_file
#
# cp data2_temp.csv data.csv
# rm data2_temp.csv
# cp data_i2_temp.csv data_i.csv
# rm data_i2_temp.csv
#
# rm data_i_temp.csv
# rm data_temp.csv
