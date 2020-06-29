(function (root, factory) {
  if (typeof define === 'function' && define.amd) {
    define(['kaitai-struct/KaitaiStream', 'bioflow-kaitai-processors/Wrapper', 'bioflow-kaitai-processors/EmptyStringWrapper'], factory);
  } else if (typeof module === 'object' && module.exports) {
    module.exports = factory(require('kaitai-struct/KaitaiStream'), require('bioflow-kaitai-processors/Wrapper'), require('bioflow-kaitai-processors/EmptyStringWrapper'));
  } else {
    root.Fcs = factory(root.KaitaiStream, root.Wrapper, root.EmptyStringWrapper);
  }
}(this, function (KaitaiStream, Wrapper, EmptyStringWrapper) {
var Fcs = (function() {
  function Fcs(_io, _parent, _root) {
    this._io = _io;
    this._parent = _parent;
    this._root = _root || this;

    this._read();
  }
  Fcs.prototype._read = function() {
    this.headerSection = new FcsHeader(this._io, this, this._root);
    this.textSection = new FcsText(this._io, this, this._root);
    this.dataSection = new FcsRawData(this._io, this, this._root);
    this.analysisSection = new FcsRawAnalysis(this._io, this, this._root);
    this.crc16 = this._io.readBytesFull();
  }

  /**
   * @see {@link http://isac-net.org/Resources/Standards/FCS3-1.aspx|Source}
   */

  var FcsHeader = Fcs.FcsHeader = (function() {
    function FcsHeader(_io, _parent, _root) {
      this._io = _io;
      this._parent = _parent;
      this._root = _root || this;

      this._read();
    }
    FcsHeader.prototype._read = function() {
      this._unnamed0 = this._io.ensureFixedContents([70, 67, 83]);
      this.fcsVersion = KaitaiStream.bytesToStr(this._io.readBytes(3), "ASCII");
      this._unnamed2 = this._io.ensureFixedContents([32, 32, 32, 32]);
      this._raw_textStart = this._io.readBytes(8);
      var _process = new Wrapper("builtins", "int");
      this.textStart = _process.decode(this._raw_textStart);
      this._raw_textEnd = this._io.readBytes(8);
      var _process = new Wrapper("builtins", "int");
      this.textEnd = _process.decode(this._raw_textEnd);
      this._raw_dataStart = this._io.readBytes(8);
      var _process = new Wrapper("builtins", "int");
      this.dataStart = _process.decode(this._raw_dataStart);
      this._raw_dataEnd = this._io.readBytes(8);
      var _process = new Wrapper("builtins", "int");
      this.dataEnd = _process.decode(this._raw_dataEnd);
      this._raw_analysisStart = this._io.readBytes(8);
      var _process = new EmptyStringWrapper(0, "builtins", "int");
      this.analysisStart = _process.decode(this._raw_analysisStart);
      this._raw_analysisEnd = this._io.readBytes(8);
      var _process = new EmptyStringWrapper(0, "builtins", "int");
      this.analysisEnd = _process.decode(this._raw_analysisEnd);
      this._unnamed9 = this._io.readBytes((Number.parseInt(this.rawTextStart, 10) - this._io.pos));
    }
    Object.defineProperty(FcsHeader.prototype, 'rawAnalysisStart', {
      get: function() {
        if (this._m_rawAnalysisStart !== undefined)
          return this._m_rawAnalysisStart;
        var _pos = this._io.pos;
        this._io.seek(42);
        this._m_rawAnalysisStart = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawAnalysisStart;
      }
    });
    Object.defineProperty(FcsHeader.prototype, 'rawTextStart', {
      get: function() {
        if (this._m_rawTextStart !== undefined)
          return this._m_rawTextStart;
        var _pos = this._io.pos;
        this._io.seek(10);
        this._m_rawTextStart = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawTextStart;
      }
    });
    Object.defineProperty(FcsHeader.prototype, 'rawDataStart', {
      get: function() {
        if (this._m_rawDataStart !== undefined)
          return this._m_rawDataStart;
        var _pos = this._io.pos;
        this._io.seek(26);
        this._m_rawDataStart = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawDataStart;
      }
    });
    Object.defineProperty(FcsHeader.prototype, 'rawAnalysisEnd', {
      get: function() {
        if (this._m_rawAnalysisEnd !== undefined)
          return this._m_rawAnalysisEnd;
        var _pos = this._io.pos;
        this._io.seek(50);
        this._m_rawAnalysisEnd = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawAnalysisEnd;
      }
    });
    Object.defineProperty(FcsHeader.prototype, 'rawTextEnd', {
      get: function() {
        if (this._m_rawTextEnd !== undefined)
          return this._m_rawTextEnd;
        var _pos = this._io.pos;
        this._io.seek(18);
        this._m_rawTextEnd = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawTextEnd;
      }
    });
    Object.defineProperty(FcsHeader.prototype, 'rawDataEnd', {
      get: function() {
        if (this._m_rawDataEnd !== undefined)
          return this._m_rawDataEnd;
        var _pos = this._io.pos;
        this._io.seek(34);
        this._m_rawDataEnd = KaitaiStream.bytesToStr(this._io.readBytes(8), "ASCII");
        this._io.seek(_pos);
        return this._m_rawDataEnd;
      }
    });

    /**
     * FCS magic number (0 - 2)
     */

    /**
     * padding
     */

    /**
     * offset to start of TEXT segment, as ASCII(UInt)
     */

    /**
     * offset to end of TEXT segment, as ASCII(UInt)
     */

    /**
     * offset to start of DATA segment, as ASCII(UInt)
     */

    /**
     * offset to end of DATA segment, as ASCII(UInt)
     */

    /**
     * offset to start of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
     */

    /**
     * offset to end of ANALYSIS segment, as ASCII(UInt). Absence is either right justified 0 or all spaces (0x20)
     */

    /**
     * padding
     */

    return FcsHeader;
  })();

  var FcsText = Fcs.FcsText = (function() {
    function FcsText(_io, _parent, _root) {
      this._io = _io;
      this._parent = _parent;
      this._root = _root || this;

      this._read();
    }
    FcsText.prototype._read = function() {
      this.delimiter = this._io.readU1();
      this.keywords = []
      var i = 0;
      do {
        var _ = new FcsKeyword(this._io, this, this._root);
        this.keywords.push(_);
        i++;
      } while (!(this._io.pos >= (Number.parseInt(this._root.headerSection.rawTextEnd, 10) - 1)));
    }

    /**
     * NB, There is a tradeoff here. Str type needs a hardcoded terminator, but omits the terminator. Repeat-until can use a dynamic terminator, but can''t omit it. We choose the latter for consistent behavior. It could maybe be done with str, terminator, parametric type, ''2 phase parsing'' as mentioned in the ''encrypted blob'' github issue Also, process() isn''t allowed. Strip the terminator in client code instead! Docs say, ''True'' array types (Arrays come from using the `repeat` syntax) and ''byte arrays'' share the same literal syntax and lots of method API, but they are actually very different types.
     */

    var FcsKeyword = FcsText.FcsKeyword = (function() {
      function FcsKeyword(_io, _parent, _root) {
        this._io = _io;
        this._parent = _parent;
        this._root = _root || this;

        this._read();
      }
      FcsKeyword.prototype._read = function() {
        this.key = []
        var i = 0;
        do {
          var _ = this._io.readU1();
          this.key.push(_);
          i++;
        } while (!( ((_ == this._parent.delimiter) || (this._io.pos >= (Number.parseInt(this._root.headerSection.rawTextEnd, 10) - 1))) ));
        this.value = []
        var i = 0;
        do {
          var _ = this._io.readU1();
          this.value.push(_);
          i++;
        } while (!( ((_ == this._parent.delimiter) || (this._io.pos >= Number.parseInt(this._root.headerSection.rawTextEnd, 10))) ));
      }

      /**
       * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
       */

      /**
       * This is a "dynamic terminator" implementation. The terminator is the last byte, WHICH MUST BE STRIPPED MANUALLY.
       */

      return FcsKeyword;
    })();

    return FcsText;
  })();

  var FcsRawData = Fcs.FcsRawData = (function() {
    function FcsRawData(_io, _parent, _root) {
      this._io = _io;
      this._parent = _parent;
      this._root = _root || this;

      this._read();
    }
    FcsRawData.prototype._read = function() {
      this._unnamed0 = this._io.readBytes((Number.parseInt(this._root.headerSection.rawDataStart, 10) - this._io.pos));
      switch ( (( ((Number.parseInt(this._root.headerSection.rawDataStart, 10) == 0) && (Number.parseInt(this._root.headerSection.rawDataEnd, 10) == 0)) ) || ((Number.parseInt(this._root.headerSection.rawDataEnd, 10) - Number.parseInt(this._root.headerSection.rawDataStart, 10)) < 0)) ) {
      case false:
        this.data = new Raw(this._io, this, this._root);
        break;
      case true:
        this.data = new Empty(this._io, this, this._root);
        break;
      }
    }

    var Empty = FcsRawData.Empty = (function() {
      function Empty(_io, _parent, _root) {
        this._io = _io;
        this._parent = _parent;
        this._root = _root || this;

        this._read();
      }
      Empty.prototype._read = function() {
        this.raw = this._io.readBytes(0);
      }

      return Empty;
    })();

    var Raw = FcsRawData.Raw = (function() {
      function Raw(_io, _parent, _root) {
        this._io = _io;
        this._parent = _parent;
        this._root = _root || this;

        this._read();
      }
      Raw.prototype._read = function() {
        this.raw = this._io.readBytes(((Number.parseInt(this._root.headerSection.rawDataEnd, 10) - Number.parseInt(this._root.headerSection.rawDataStart, 10)) + 1));
      }

      return Raw;
    })();

    /**
     * padding
     */

    /**
     * NB, this fails on data section offsets specified only in TEXT section. Also does not support DATA segments of length 1. NB2: We process + patch raw_data_* to values from the text section
     */

    return FcsRawData;
  })();

  /**
   * NB, this fails on analysis section offsets specified only in TEXT section. It also does not allow sections of length 1!
   */

  var FcsRawAnalysis = Fcs.FcsRawAnalysis = (function() {
    function FcsRawAnalysis(_io, _parent, _root) {
      this._io = _io;
      this._parent = _parent;
      this._root = _root || this;

      this._read();
    }
    FcsRawAnalysis.prototype._read = function() {
      switch ( (( ((this._root.headerSection.rawAnalysisStart == Array.from(this._root.headerSection.rawAnalysisStart).reverse().join('')) && (this._root.headerSection.rawAnalysisEnd == Array.from(this._root.headerSection.rawAnalysisEnd).reverse().join(''))) ) || ( ((Number.parseInt(this._root.headerSection.rawAnalysisStart, 10) == 0) && (Number.parseInt(this._root.headerSection.rawAnalysisEnd, 10) == 0)) )) ) {
      case false:
        this.analysis = new Raw(this._io, this, this._root);
        break;
      case true:
        this.analysis = new Empty(this._io, this, this._root);
        break;
      }
    }

    var Empty = FcsRawAnalysis.Empty = (function() {
      function Empty(_io, _parent, _root) {
        this._io = _io;
        this._parent = _parent;
        this._root = _root || this;

        this._read();
      }
      Empty.prototype._read = function() {
        this.raw = this._io.readBytes(0);
      }

      return Empty;
    })();

    var Raw = FcsRawAnalysis.Raw = (function() {
      function Raw(_io, _parent, _root) {
        this._io = _io;
        this._parent = _parent;
        this._root = _root || this;

        this._read();
      }
      Raw.prototype._read = function() {
        this.raw = this._io.readBytes(((Number.parseInt(this._root.headerSection.rawAnalysisEnd, 10) - Number.parseInt(this._root.headerSection.rawAnalysisStart, 10)) + 1));
      }

      /**
       * NB, this does not allow sections of length 1!
       */

      return Raw;
    })();

    return FcsRawAnalysis;
  })();

  /**
   * NB, seq omits OTHER sections entirely.
   * NB, CRC16 will contain the OTHER segments if they exist!
   * In practice many formats put OTHER stuff in TEXT keywords...
   * The CRC word is computed for the part of each data set
   * beginning with the first byte of the HEADER segment
   * and ending with the last byte of the final segment of the data set
   * (which could be a TEXT, DATA, ANALYSIS or OTHER segment). 
   * This CRC uses the CCITT polynomial X16 + X12 + X5
   * and requires that each input character be interpreted
   * as its bit-reversed image. These requirements are satisfied
   * by the icrc function (8) if the last two function arguments are 0 and -1, respectively.
   * The CRC value will be placed as ASCII in the 8 bytes immediately after the last segment of the data set.
   * If an implementor chooses not to compute and store a CRC word
   * then the 8 bytes immediately after the last segment of the data set
   * should be filled with ASCII '0' characters.
   * NB, it is not actually validated in this parser.
   * That would use a process() on a raw stream copy of file,
   * probably as a root level instance w/ pos+size.
   */

  return Fcs;
})();
return Fcs;
}));