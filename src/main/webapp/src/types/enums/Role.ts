export enum Role {
  owner = 'OWNER',
  editor = 'EDITOR',
  readonly = 'READONLY',
}

const getRole = (role: string): Role => {
  switch (role.toUpperCase()) {
    case Role.owner.toString():
      return Role.owner;
    case Role.editor.toString():
      return Role.editor;
    case Role.readonly.toString():
      return Role.readonly;
    default:
      throw new Error(`No matching role found for : ${role.toUpperCase()}`);
  }
};

export { getRole };
