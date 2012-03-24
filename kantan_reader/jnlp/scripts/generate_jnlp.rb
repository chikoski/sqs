#!/usr/bin/env ruby

require 'rubygems'
require 'rexml/document'
require 'erb'
require 'yaml'
require 'optparse'

class Hash
  
  def symbolize_keys
    product ={}
    self.each do |k, v|
      product[k.to_sym] = v
    end
    return product
  end
  
end

class Worker
  
  def initialize(opt={})
    @pom = opt[:pom]
    @dirs = {
      :base => opt[:directories][:base] || ".",
      :template => opt[:directories][:template] || "templates",
      :target => opt[:directories][:target] || "."
    }
    @templates = opt[:templates] || []

    p @dirs
    p @templates
  end

  def start
    create_jnlp
    serialize_jnlp
  end

  protected
  
  def expand_path(file, base=nil)
    base = @dirs[base] || ""
    return File.expand_path(File.join(@dirs[:base], base, file))
  end
  
  def serialize_jnlp
    create_jnlp unless jnlp_created?
    @jnlp.each{|key, jnlp|
      filename = File.basename(key).gsub(/\.erb/, "")
      File.open(expand_path(filename, :target), "w"){|fd|
        fd.write(jnlp)
      }
    }
  end

  def jnlp_created?
    return @templates && @jnlp && @jnlp.length > 0&& @jnlp.length == @templates.length
  end
  
  def create_jnlp
    return @jnlp if @jnlp
    @jnlp = {}
    raise unless template_available?
    dependency_list
    @templates.each do |template|
      template = expand_path(template, :template)
      t = ERB.new(File.read(template))
      @jnlp[template] = t.result(binding)
    end
  end

  def dependency_list
    unless @dependencies
      e = DependencyList.new(:pom => @pom)
      @dependencies = e.extract
    end
    return @dependencies
  end

  def template_available?
    return @templates && @templates.length > 0 && @templates.all?{|v| File.readable?(expand_path(v, :template))}
  end
  
end


class DependencyList
  include REXML
  attr_reader :result
  
  def initialize(opt={})
    @pom = opt[:pom] || ""
  end

  def extract
    raise unless test_prerequisites
    run
    return @result
  end
  
  protected
  def run
    @result = []
    doc = Document.new(File.new(@pom))
    doc.elements.each("project/dependencies/dependency"){|elm|
      d = {
        :groupId => nil,
        :artifactId => nil,
        :version => nil
      }
      d.keys.each{|k|
        elm.each_element(k.to_s){|c|
          d[k] = c.text
        }
      }
      @result << d
    }
  end

  def test_prerequisites
    return test_pom_file
  end

  def test_pom_file
    return @pom && File.exist?(@pom) && File.readable?(@pom)
  end
  
end

def load_config(filename)
  conf ={}
  conf = YAML.load_file(filename).symbolize_keys
  conf[:templates] ||= []
  conf[:directories] = (conf[:directories] || {}).symbolize_keys
  return conf
end

def usage
  puts "generate_jnlp.rb [-f config_file] pom.xml"
end

config_file = File.expand_path("config.yaml", File.dirname(__FILE__))
pom = nil

parser = OptionParser.new
parser.on("-f config_file.yaml"){|v|
  config_file = v
}
parser.parse!(ARGV)
pom = ARGV.shift

if pom && File.readable?(pom) && File.readable?(config_file)
  conf = load_config(config_file)
  conf[:pom] = pom
  worker = Worker.new(conf)
  worker.start
else
  usage
end

0

