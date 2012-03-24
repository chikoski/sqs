#!/usr/bin/env ruby

puts ARGV.map{|f|
  begin
    File.read(f)
  rescue
    ""
  end
}.map{|file|
  file.split(/\n/)
}.flatten.map{|logline|
  File.basename(logline.gsub(/jar.*$/, "").gsub(/^.+?GET/, "")).to_s + "jar"
}.uniq
