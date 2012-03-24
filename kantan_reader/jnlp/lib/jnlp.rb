require 'rexml/document'

class JNLP

  def self.create(file)
    
    doc = REXML::Document.new(File.new(file))
    
    opt={
      :jar_files => []
    }

    doc.elements.each("jnlp/resources/jar"){|jar|
      jar_file = jar.attribute("href") 
      opt[:jar_files] << jar_file.to_s.gsub(/\s+/, "") if jar_file
    }

    return self.new(opt)
  end

  attr_reader :jar_files

  def initialize(opt={})
    
    @jar_files = opt[:jar_files] || []

  end
  
  
end
  
