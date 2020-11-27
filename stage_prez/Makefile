PROJECT=stage_prez
TEX=latexmk -pdf
#VISUAL=/cygdrive/C/Program\ Files\ \(x86\)/Adobe/Reader\ 11.0/Reader/AcroRd32.exe
VISUAL=evince

all: export max_print_line=10000
all:
	$(TEX) $(PROJECT).tex
	$(VISUAL) $(PROJECT).pdf &

clean:
	latexmk -C -bibtex -quiet
