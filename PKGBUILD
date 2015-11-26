#
# SimGrid: Grid and Cloud Simulator
#
# Maintainer: Guillaume Turchini <guillaume.turchini@gmail.com>"

_pkgname=simgrid
pkgname=${_pkgname}-java-git
pkgver=r18497.bb31d01
pkgrel=1
pkgdesc='Grid and Cloud Simulator (with java)'
arch=('i686' 'x86_64')
url='http://simgrid.gforge.inria.fr/index.html'
license=('GPL')
source=("${_pkgname}::git://scm.gforge.inria.fr/simgrid/simgrid.git")
depends=('boost-libs' 'java-runtime')
makedepends=('cmake' 'git' 'boost' 'java-environment')
md5sums=('SKIP')
conflicts=("${_pkgname}")
provides=("${_pkgname}")

pkgver() {
  cd "${_pkgname}"
  printf "r%s.%s" "$(git rev-list --count HEAD)" "$(git rev-parse --short HEAD)"
}

package() {
  mkdir -p $pkgdir/opt/simgrid
  cd $srcdir/${_pkgname}/
  cmake -DCMAKE_INSTALL_PREFIX=$pkgdir/opt/simgrid -Denable_documentation=OFF -Denable_java=ON -DJAVA_HOME=/usr/lib/jvm/default .
  make
  make install
}
