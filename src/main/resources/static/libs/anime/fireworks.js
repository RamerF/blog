$(function() {
  const numberOfParticules = 20;
  const minOrbitRadius = 20;
  const maxOrbitRadius = 25;

  const minCircleRadius = 5;
  const maxCircleRadius = 10;

  const minAnimeDuration = 500;
  const maxAnimeDuration = 1200;

  const minDiffuseRadius = 30;
  const maxDiffuseRadius = 40;

  let canvasEl = document.getElementById('fireworks');
  let ctx = canvasEl.getContext('2d');
  let pointerX = 0;
  let pointerY = 0;

  let tap =
      'ontouchstart' in window || navigator.msMaxTouchPoints
          ? 'touchstart'
          : 'mousedown';

  let colors = [
    '255, 175, 73',
    '185, 246, 202',
    '230, 74, 25',
    '64, 196, 255',
    '161, 136, 127'];

  function setCanvasSize() {
    canvasEl.width = window.innerWidth;
    canvasEl.height = window.innerHeight;
    canvasEl.style.width = window.innerWidth + 'px';
    canvasEl.style.height = window.innerHeight + 'px';
  }

  function updateCoords(e) {
    pointerX = e.clientX || e.touches[0].clientX;
    pointerY = e.clientY || e.touches[0].clientY;
  }

  function setParticuleDirection(p) {
    let angle = (anime.random(0, 360) * Math.PI) / 180;
    let value = anime.random(minDiffuseRadius, maxDiffuseRadius);
    let radius = [-1, 1][anime.random(0, 1)] * value;
    return {
      x: p.x + radius * Math.cos(angle),
      y: p.y + radius * Math.sin(angle),
    };
  }

  function createParticule(x, y) {
    let p = {};
    p.x = x;
    p.y = y;
    p.color =
        'rgba(' +
        colors[anime.random(0, colors.length - 1)] +
        ',' +
        anime.random(0.2, 0.8) +
        ')';
    p.radius = anime.random(minCircleRadius, maxCircleRadius);
    p.endPos = setParticuleDirection(p);
    p.draw = function() {
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.radius, 0, 2 * Math.PI, true);
      ctx.fillStyle = p.color;
      ctx.fill();
    };
    return p;
  }

  function createCircle(x, y) {
    let p = {};
    p.x = x;
    p.y = y;
    p.color = '#000';
    p.radius = 0.1;
    p.alpha = 0.5;
    p.lineWidth = 6;
    p.draw = function() {
      ctx.globalAlpha = p.alpha;
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.radius, 0, 2 * Math.PI, true);
      ctx.lineWidth = p.lineWidth;
      ctx.strokeStyle = p.color;
      ctx.stroke();
      ctx.globalAlpha = 1;
    };
    return p;
  }

  function renderParticule(anim) {
    for (let i = 0; i < anim.animatables.length; i++) {
      anim.animatables[i].target.draw();
    }
  }

  function animateParticules(x, y) {
    x = x - 8;
    y = y - 8;
    let circle = createCircle(x, y);
    let particules = [];
    for (let i = 0; i < numberOfParticules; i++) {
      particules.push(createParticule(x, y));
    }
    anime.timeline().add({
      targets: particules,
      x: function(p) {
        return p.endPos.x;
      },
      y: function(p) {
        return p.endPos.y;
      },
      radius: 0.1,
      duration: anime.random(minAnimeDuration, maxAnimeDuration),
      easing: 'easeOutExpo',
      update: renderParticule,
    }).add({
      targets: circle,
      radius: anime.random(minOrbitRadius, maxOrbitRadius),
      lineWidth: 0,
      alpha: {
        value: 0,
        easing: 'linear',
        duration: anime.random(600, 800),
      },
      duration: anime.random(1200, 1800),
      easing: 'easeOutExpo',
      update: renderParticule,
      offset: 0,
    });
  }

  let render = anime({
    duration: Infinity,
    update: function() {
      ctx.clearRect(0, 0, canvasEl.width, canvasEl.height);
    },
  });
  document.addEventListener(
      tap,
      function(e) {
        render.play();
        updateCoords(e);
        animateParticules(pointerX, pointerY);
      },
      false,
  );

  setCanvasSize();
  window.addEventListener('resize', setCanvasSize, false);
});
