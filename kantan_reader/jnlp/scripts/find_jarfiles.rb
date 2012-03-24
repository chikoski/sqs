#!/usr/bin/env ruby

require File.expand_path("../config/environment", File.dirname(__FILE__))
require 'jnlp'
require 'find'
require 'fileutils'
require 'optparse'

def usage
  STDERR.puts "find_jarfiles.rb [--repository mvn repository] [--dest dest dir] JNLP [JNLP...]"
  exit
end

def unwritable(dirname)
  STDERR.puts "#{dirname} is unwritable"
  exit
end

def show_stats(required, copied)
  not_copied = (required - copied.map{|f| File.basename(f)})
  puts "#{copied.length} files were copied"
  puts "#{not_copied.length} files are required, but not copied."
  puts "Each name of the files are listed as follows"
  not_copied.each do |j|
    puts "- #{j}"
  end
end

def show_settings(config)
	puts "Repository: #{config[:repository]}"
	puts "Dest: #{config[:dest]}"
end


usage if ARGV.length == 0

config = {
  :repository => ".",
  :dest => "./jarfiles"
}
opt = OptionParser.new
opt.on("--repository repository"){|rep|
  config[:repository] = rep
}
opt.on("--dest dest"){|dest|
  config[:dest] = dest
}

opt.parse!(ARGV)

unwritable(config[:dest]) unless File.writable?(config[:dest])

show_settings(config)

jar_files =  ARGV.map{|jnlp|
  JNLP.create(jnlp).jar_files
}.flatten

availables = []

Find.find(config[:repository]){|f|
  basename = File.basename(f)
  availables << f if jar_files.index(basename)
}

availables.each do |jar|
  basename = File.basename(jar)
  dest = File.expand_path(File.join(config[:dest], basename))
  FileUtils.cp(jar, dest)
end

show_stats(jar_files, availables)
