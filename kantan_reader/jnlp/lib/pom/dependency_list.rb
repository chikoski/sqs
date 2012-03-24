require 'rexml/document'
require 'pom/dependency'
require 'yaml'

module Pom

  class DependencyList
    include REXML
    
    def self.create(file, format=nil)
      format = File.extname(file).gsub(/^./, "").to_sym || :xml
      if format == :yaml
        return create_from_yaml(file)
      else
        return create_from_xml(file)
      end
    end

    def self.create_from_yaml(file)
      return YAML.load_file(file)
    end

    def self.create_from_xml(file)
      doc = Document.new(File.new(file))
      ret = []
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
        ret << Dependency.new(d)
      }
      return ret
    end
    
  end

end
