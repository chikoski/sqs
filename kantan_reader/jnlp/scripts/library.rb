#!/usr/bin/ruby

$LOAD_PATH  << File.expand_path("../lib", File.dirname(__FILE__))

require 'pom'
require 'erb'

@dependencies = ARGV.map{|yaml|
  list = Pom::DependencyList.create(yaml, :yaml)
  list.map{|i| i.filename}
}.flatten.uniq

template_path = File.expand_path("../templates/libraries.jnlp.erb", File.dirname(__FILE__))
template = File.read(template_path)
erb = ERB.new(template)
puts erb.result(binding)

