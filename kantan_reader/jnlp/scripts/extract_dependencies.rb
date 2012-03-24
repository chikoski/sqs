#!/usr/bin/ruby

$LOAD_PATH  << File.expand_path("../lib", File.dirname(__FILE__))

require 'yaml'
require 'pom'

list = Pom::DependencyList.create(ARGV.shift)
puts list.to_yaml


