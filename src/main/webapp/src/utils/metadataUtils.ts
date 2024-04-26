import type { File } from '@/types'

function isStarred(file: File | undefined): boolean {
  return file && file.metadata.length > 0 ? file.metadata[0].starred : undefined
}

function toggleStarred(file: File): void {
  file.metadata[0].starred = !isStarred(file)
}

function toggleStarredInArray(files: Array<File>, fileId: number): void {
  const file = files.find(file => file.id === fileId)
  if (!file)
    return
  toggleStarred(file)
}

export { isStarred, toggleStarred, toggleStarredInArray }
