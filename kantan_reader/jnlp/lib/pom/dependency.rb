module Pom
  
  class Dependency

    attr_reader :group_id, :artifact_id, :version

    def initialize(hash)
      @group_id = hash[:group_id] || hash["group_id"] || hash[:groupId] || hash["groupId"]
      @artifact_id = hash[:artifact_id] || hash["artifact_id"] || hash[:artifactId] || hash["artifactId"]
      @version = hash[:version] || hash["version"]
    end

    def filename
      return "#{@artifact_id}-#{@version}.jar"
    end

    def to_string
      return "#{@group_id}:#{@artifact_id}:#{@version}"
    end

  end

end
