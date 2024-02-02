const styleObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type === 'attributes' && mutation.attributeName === 'style') {
      document.body.removeAttribute('style');
    }
  }
});

const headObserver = new MutationObserver((mutationList: Array<MutationRecord>) => {
  for (const mutation of mutationList) {
    if (mutation.type == 'childList' && mutation.addedNodes.length == 1) {
      if (mutation.addedNodes[0].textContent?.includes('--removed-body-scroll-bar-size')) {
        document.head.removeChild(mutation.addedNodes[0]);
      }
    }
  }
});

export { styleObserver, headObserver };
