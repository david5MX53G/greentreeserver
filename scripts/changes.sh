git log --since '2018-07-22' --pretty=format:'%cd "%s"' | sed -e 's/$/  /g' > CHANGES; python -m markdown CHANGES
