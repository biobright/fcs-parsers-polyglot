require 'kaitai/struct/struct'

unless Gem::Version.new(Kaitai::Struct::VERSION) >= Gem::Version.new('0.7')
  raise "Incompatible Kaitai Struct Ruby API: 0.7 or later is required, but you have #{Kaitai::Struct::VERSION}"
end

class Fcs < Kaitai::Struct::Struct
  def initialize(_io, _parent = nil, _root = self)
    super(_io, _parent, _root)
    _read
  end

  def _read
    @header_section = FcsHeader.new(@_io, self, @_root)
    @text_section = FcsText.new(@_io, self, @_root)
    @data_section = FcsRawData.new(@_io, self, @_root)
    @analysis_section = FcsRawAnalysis.new(@_io, self, @_root)
    @crc16 = @_io.read_bytes_full
    self
  end

  ##
  # @see http://isac-net.org/Resources/Standards/FCS3-1.aspx Source
  class FcsHeader < Kaitai::Struct::Struct
    def initialize(_io, _parent = nil, _root = self)
      super(_io, _parent, _root)
      _read
    end

    def _read
      @_unnamed0 = @_io.ensure_fixed_contents([70, 67, 83].pack('C*'))
      @fcs_version = (@_io.read_bytes(3)).force_encoding("ASCII")
      @_unnamed2 = @_io.ensure_fixed_contents([32, 32, 32, 32].pack('C*'))
      @_raw_text_start = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::Wrapper.new("builtins", "int")
      @text_start = _process.decode(@_raw_text_start)
      @_raw_text_end = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::Wrapper.new("builtins", "int")
      @text_end = _process.decode(@_raw_text_end)
      @_raw_data_start = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::Wrapper.new("builtins", "int")
      @data_start = _process.decode(@_raw_data_start)
      @_raw_data_end = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::Wrapper.new("builtins", "int")
      @data_end = _process.decode(@_raw_data_end)
      @_raw_analysis_start = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::EmptyStringWrapper.new(0, "builtins", "int")
      @analysis_start = _process.decode(@_raw_analysis_start)
      @_raw_analysis_end = @_io.read_bytes(8)
      _process = Bioflow::Kaitai::Processors::EmptyStringWrapper.new(0, "builtins", "int")
      @analysis_end = _process.decode(@_raw_analysis_end)
      @_unnamed9 = @_io.read_bytes((raw_text_start.to_i - _io.pos))
      self
    end
    def raw_analysis_start
      return @raw_analysis_start unless @raw_analysis_start.nil?
      _pos = @_io.pos
      @_io.seek(42)
      @raw_analysis_start = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_analysis_start
    end
    def raw_text_start
      return @raw_text_start unless @raw_text_start.nil?
      _pos = @_io.pos
      @_io.seek(10)
      @raw_text_start = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_text_start
    end
    def raw_data_start
      return @raw_data_start unless @raw_data_start.nil?
      _pos = @_io.pos
      @_io.seek(26)
      @raw_data_start = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_data_start
    end
    def raw_analysis_end
      return @raw_analysis_end unless @raw_analysis_end.nil?
      _pos = @_io.pos
      @_io.seek(50)
      @raw_analysis_end = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_analysis_end
    end
    def raw_text_end
      return @raw_text_end unless @raw_text_end.nil?
      _pos = @_io.pos
      @_io.seek(18)
      @raw_text_end = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_text_end
    end
    def raw_data_end
      return @raw_data_end unless @raw_data_end.nil?
      _pos = @_io.pos
      @_io.seek(34)
      @raw_data_end = (@_io.read_bytes(8)).force_encoding("ASCII")
      @_io.seek(_pos)
      @raw_data_end
    end

    ##
    # FCS magic number (0 - 2)
    attr_reader :_unnamed0
    attr_reader :fcs_version

    ##
    # padding
    attr_reader :_unnamed2

    ##
    # offset to start of TEXT segment, as ASCII(UInt)
    attr_reader :text_start

    ##
    # offset to end of TEXT segment, as ASCII(UInt)
    attr_reader :text_end

    ##
    # offset to start of DATA segment, as ASCII(UInt)
    attr_reader :data_start

    ##
    # offset to end of DATA segment, as ASCII(UInt)
    attr_reader :data_end

    ##
    # offset to start of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
    attr_reader :analysis_start

    ##
    # offset to end of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
    attr_reader :analysis_end

    ##
    # padding
    attr_reader :_unnamed9
    attr_reader :_raw_text_start
    attr_reader :_raw_text_end
    attr_reader :_raw_data_start
    attr_reader :_raw_data_end
    attr_reader :_raw_analysis_start
    attr_reader :_raw_analysis_end
  end
  class FcsText < Kaitai::Struct::Struct
    def initialize(_io, _parent = nil, _root = self)
      super(_io, _parent, _root)
      _read
    end

    def _read
      @delimiter = @_io.read_u1
      @keywords = []
      i = 0
      begin
        _ = FcsKeyword.new(@_io, self, @_root)
        @keywords << _
        i += 1
      end until _io.pos >= (_root.header_section.raw_text_end.to_i - 1)
      self
    end

    ##
    # NB, There is a tradeoff here. Str type needs a hardcoded terminator, but omits the terminator. Repeat-until can use a dynamic terminator, but can''t omit it. We choose the latter for consistent behavior. It could maybe be done with str, terminator, parametric type, ''2 phase parsing'' as mentioned in the ''encrypted blob'' github issue Also, process() isn''t allowed. Strip the terminator in client code instead! Docs say, ''True'' array types (Arrays come from using the `repeat` syntax) and ''byte arrays'' share the same literal syntax and lots of method API, but they are actually very different types.
    class FcsKeyword < Kaitai::Struct::Struct
      def initialize(_io, _parent = nil, _root = self)
        super(_io, _parent, _root)
        _read
      end

      def _read
        @key = []
        i = 0
        begin
          _ = @_io.read_u1
          @key << _
          i += 1
        end until  ((_ == _parent.delimiter) || (_io.pos >= (_root.header_section.raw_text_end.to_i - 1))) 
        @value = []
        i = 0
        begin
          _ = @_io.read_u1
          @value << _
          i += 1
        end until  ((_ == _parent.delimiter) || (_io.pos >= _root.header_section.raw_text_end.to_i)) 
        self
      end

      ##
      # This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
      attr_reader :key

      ##
      # This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
      attr_reader :value
    end
    attr_reader :delimiter
    attr_reader :keywords
  end
  class FcsRawData < Kaitai::Struct::Struct
    def initialize(_io, _parent = nil, _root = self)
      super(_io, _parent, _root)
      _read
    end

    def _read
      @_unnamed0 = @_io.read_bytes((_root.header_section.raw_data_start.to_i - _io.pos))
      case  (( ((_root.header_section.raw_data_start.to_i == 0) && (_root.header_section.raw_data_end.to_i == 0)) ) || ((_root.header_section.raw_data_end.to_i - _root.header_section.raw_data_start.to_i) < 0)) 
      when false
        @data = Raw.new(@_io, self, @_root)
      when true
        @data = Empty.new(@_io, self, @_root)
      end
      self
    end
    class Empty < Kaitai::Struct::Struct
      def initialize(_io, _parent = nil, _root = self)
        super(_io, _parent, _root)
        _read
      end

      def _read
        @raw = @_io.read_bytes(0)
        self
      end
      attr_reader :raw
    end
    class Raw < Kaitai::Struct::Struct
      def initialize(_io, _parent = nil, _root = self)
        super(_io, _parent, _root)
        _read
      end

      def _read
        @raw = @_io.read_bytes(((_root.header_section.raw_data_end.to_i - _root.header_section.raw_data_start.to_i) + 1))
        self
      end
      attr_reader :raw
    end

    ##
    # padding
    attr_reader :_unnamed0

    ##
    # NB, this fails on data section offsets specified only in TEXT section. Also does not support DATA segments of length 1. NB2: We process + patch raw_data_* to values from the text section
    attr_reader :data
  end

  ##
  # NB, this fails on analysis section offsets specified only in TEXT section. It also does not allow sections of length 1!
  class FcsRawAnalysis < Kaitai::Struct::Struct
    def initialize(_io, _parent = nil, _root = self)
      super(_io, _parent, _root)
      _read
    end

    def _read
      case  (( ((_root.header_section.raw_analysis_start == _root.header_section.raw_analysis_start.reverse) && (_root.header_section.raw_analysis_end == _root.header_section.raw_analysis_end.reverse)) ) || ( ((_root.header_section.raw_analysis_start.to_i == 0) && (_root.header_section.raw_analysis_end.to_i == 0)) )) 
      when false
        @analysis = Raw.new(@_io, self, @_root)
      when true
        @analysis = Empty.new(@_io, self, @_root)
      end
      self
    end
    class Empty < Kaitai::Struct::Struct
      def initialize(_io, _parent = nil, _root = self)
        super(_io, _parent, _root)
        _read
      end

      def _read
        @raw = @_io.read_bytes(0)
        self
      end
      attr_reader :raw
    end
    class Raw < Kaitai::Struct::Struct
      def initialize(_io, _parent = nil, _root = self)
        super(_io, _parent, _root)
        _read
      end

      def _read
        @raw = @_io.read_bytes(((_root.header_section.raw_analysis_end.to_i - _root.header_section.raw_analysis_start.to_i) + 1))
        self
      end

      ##
      # NB, this does not allow sections of length 1!
      attr_reader :raw
    end
    attr_reader :analysis
  end
  attr_reader :header_section
  attr_reader :text_section
  attr_reader :data_section
  attr_reader :analysis_section

  ##
  # NB, seq omits OTHER sections entirely.
  # NB, CRC16 will contain the OTHER segments if they exist!
  # In practice many formats put OTHER stuff in TEXT keywords...
  # The CRC word is computed for the part of each data set
  # beginning with the first byte of the HEADER segment
  # and ending with the last byte of the final segment of the data set
  # (which could be a TEXT, DATA, ANALYSIS or OTHER segment). 
  # This CRC uses the CCITT polynomial X16 + X12 + X5
  # and requires that each input character be interpreted
  # as its bit-reversed image. These requirements are satisfied
  # by the icrc function (8) if the last two function arguments are 0 and -1, respectively.
  # The CRC value will be placed as ASCII in the 8 bytes immediately after the last segment of the data set.
  # If an implementor chooses not to compute and store a CRC word
  # then the 8 bytes immediately after the last segment of the data set
  # should be filled with ASCII '0' characters.
  # NB, it is not actually validated in this parser.
  # That would use a process() on a raw stream copy of file,
  # probably as a root level instance w/ pos+size.
  attr_reader :crc16
end
